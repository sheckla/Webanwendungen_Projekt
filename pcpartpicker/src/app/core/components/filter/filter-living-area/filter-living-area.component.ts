import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, FormControl, NG_VALUE_ACCESSOR, ReactiveFormsModule } from '@angular/forms';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { filter, map, pairwise, startWith, switchMap, tap } from 'rxjs';

import { CommonModule } from '@angular/common';
import { FilterSelectComponent } from '../filter-select/filter-select.component';
import { GlobalInstitutionService } from '@framework/services/global-institution';
import { LivingArea } from '@framework/models/living-area.model';
import { LivingAreaService } from '@framework/services/living-area/living-area.service';
import { SelectOption } from '@framework/types/select-option';
import { UUID } from '@framework/types/uuid';

@UntilDestroy()
@Component({
  selector: 'app-filter-living-area',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FilterSelectComponent,],
  templateUrl: './filter-living-area.component.html',
  styleUrls: ['./filter-living-area.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => FilterLivingAreaComponent),
      multi: true
    }
  ],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FilterLivingAreaComponent implements OnInit, ControlValueAccessor {
  /** Allow multiple selection. Default is `false`. */
  @Input() multiple: boolean = false;
  /** Selected option can be cleared. Default is `true`. */
  @Input() clearable: boolean = true;
  /** ID to associate the label with the control. */
  @Input() labelForId: string | undefined;

  /** Array of available options to select. */
  public livingAreaOptions: SelectOption<UUID>[] | null = null;

  /** Living area form control */
  public readonly livingArea = new FormControl<string | string[] | null>(null);

  public onTouched: () => void = () => { };

  constructor(
    private institutionService: GlobalInstitutionService,
    private livingAreaService: LivingAreaService,
    private changeDetectorRef: ChangeDetectorRef,
  ) { }

  ngOnInit(): void {
    this.setupOptions();
  }

  writeValue(val: any): void {
    this.livingArea.setValue(val, { emitEvent: false });
  }

  registerOnChange(fn: any): void {
    this.livingArea.valueChanges.pipe(
      untilDestroyed(this)
    ).subscribe(fn);
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    isDisabled ? this.livingArea.disable() : this.livingArea.enable();
  }

  /**
   * Loads and prepares the available options.
   */
  private setupOptions(): void {
    this.institutionService.selectedInstitution$.pipe(
      filter(institution => !!institution),
      startWith(null),
      pairwise(),
      tap(([previousValue, currentValue]) => {
        // Reset current selected value and options when institution is changed.
        if (previousValue && currentValue !== previousValue) {
          this.livingArea.reset();
          this.setOptions(null);
        }
      }),
      map(([, currentValue]) => currentValue),
      switchMap(institution => {
        return this.livingAreaService.getLivingAreas(institution);
      }),
      map(livingAreas => {
        return livingAreas?.map((livingArea: LivingArea): SelectOption<UUID> => {
          return { label: livingArea.name, value: livingArea.id };
        });
      }),
      tap(options => this.setOptions(options)),
      untilDestroyed(this),
    ).subscribe();
  }

  /**
   * Set options for selection.
   * @param options Array of selection options to set.
   */
  private setOptions(options: SelectOption<UUID>[] | null): void {
    this.livingAreaOptions = options;
    this.changeDetectorRef.markForCheck();
  }
}
