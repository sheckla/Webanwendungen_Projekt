import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, FormControl, NG_VALUE_ACCESSOR, ReactiveFormsModule } from '@angular/forms';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { map, tap } from 'rxjs';

import { CommonModule } from '@angular/common';
import { FilterSelectComponent } from '../filter-select/filter-select.component';
import { GlobalInstitutionService } from '@framework/services/global-institution';
import { Institution } from '@framework/models/institution.model';
import { SelectOption } from '@framework/types/select-option';
import { UUID } from '@framework/types/uuid';

@UntilDestroy()
@Component({
  selector: 'app-filter-institution',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FilterSelectComponent],
  templateUrl: './filter-institution.component.html',
  styleUrls: ['./filter-institution.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => FilterInstitutionComponent),
      multi: true
    }
  ],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FilterInstitutionComponent implements OnInit, ControlValueAccessor {
  /** Allow multiple selection. Default is `false`. */
  @Input() multiple: boolean = false;
  /** Selected option can be cleared. Default is `true`. */
  @Input() clearable: boolean = true;
  /** ID to associate the label with the control. */
  @Input() labelForId: string | undefined;

  /** Array of available options to select. */
  public institutionOptions: SelectOption<UUID>[] | undefined = undefined;

  /** Institution form control */
  public readonly institution = new FormControl<string | string[] | null>(null);

  public onTouched: () => void = () => { };

  constructor(
    private institutionService: GlobalInstitutionService,
    private changeDetectorRef: ChangeDetectorRef,
  ) { }

  ngOnInit(): void {
    this.setupOptions();
  }

  writeValue(val: any): void {
    this.institution.setValue(val, { emitEvent: false });
  }

  registerOnChange(fn: any): void {
    this.institution.valueChanges.pipe(
      untilDestroyed(this)
    ).subscribe(fn);
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    isDisabled ? this.institution.disable() : this.institution.enable();
  }

  /**
   * Loads and prepares the available options.
   */
  private setupOptions(): void {
    this.institutionService.getInstitutions().pipe(
      map(institutions => {
        return institutions?.map((institution: Institution): SelectOption<UUID> => {
          return { label: `${institution.apetito_id} - ${institution.name}`, value: institution.id };
        });
      }),
      tap(options => {
        this.institutionOptions = options;
        this.changeDetectorRef.markForCheck();
      }),
      untilDestroyed(this),
    ).subscribe();
  }
}
