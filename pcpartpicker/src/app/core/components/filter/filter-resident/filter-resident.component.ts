import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, FormControl, NG_VALUE_ACCESSOR, ReactiveFormsModule } from '@angular/forms';
import { Observable, combineLatest, distinctUntilChanged, filter, map, of, pairwise, startWith, switchMap, tap } from 'rxjs';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';

import { CommonModule } from '@angular/common';
import { CustomerService } from '@framework/services/customer/customer.service';
import { FilterSelectComponent } from '../filter-select/filter-select.component';
import { GlobalInstitutionService } from '@framework/services/global-institution';
import { Resident } from '@framework/models/resident.model';
import { SelectOption } from '@framework/types/select-option';
import { UUID } from '@framework/types/uuid';

@UntilDestroy()
@Component({
  selector: 'app-filter-resident',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FilterSelectComponent],
  templateUrl: './filter-resident.component.html',
  styleUrls: ['./filter-resident.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => FilterResidentComponent),
      multi: true
    }
  ],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FilterResidentComponent implements OnInit, ControlValueAccessor {
  /**
   * Observable with a living area id to show only residents from the given
   * living area.
   */
  @Input() livingArea: Observable<UUID | null> | undefined;
  /** Allow multiple selection. Default is `false`. */
  @Input() multiple: boolean = false;
  /** Selected option can be cleared. Default is `true`. */
  @Input() clearable: boolean = true;
  /** ID to associate the label with the control. */
  @Input() labelForId: string | undefined;

  /** Array of available options to select. */
  public residentOptions: SelectOption<UUID>[] | undefined = undefined;

  /** Resident form control */
  public readonly resident = new FormControl<string | string[] | null>(null);

  public onTouched: () => void = () => { };

  constructor(
    private institutionService: GlobalInstitutionService,
    private customerService: CustomerService,
    private changeDetectorRef: ChangeDetectorRef,
  ) { }

  ngOnInit(): void {
    this.setupOptions();
  }

  writeValue(val: any): void {
    this.resident.setValue(val, { emitEvent: false });
  }

  registerOnChange(fn: any): void {
    this.resident.valueChanges.pipe(
      untilDestroyed(this)
    ).subscribe(fn);
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    isDisabled ? this.resident.disable() : this.resident.enable();
  }

  /**
   * Loads and prepares the available options.
   */
  private setupOptions(): void {
    combineLatest({
      selectedInstitution: this.institutionService.selectedInstitution$,
      livingArea: this.livingArea?.pipe(startWith(null), distinctUntilChanged()) ?? of(null),
    }).pipe(
      startWith(null),
      pairwise(),
      tap(([previousValue, currentValue]) => {
        // Reset current selected value and options when institution is changed.
        if (previousValue?.selectedInstitution && previousValue?.livingArea && (currentValue?.livingArea !== previousValue?.livingArea || currentValue?.selectedInstitution !== previousValue?.selectedInstitution)) {
          this.resident.reset();
          this.setOptions(null);
        }
      }),
      map(([, currentValue]) => currentValue),
      filter(data => !!data?.selectedInstitution),
      switchMap(data => {
        return this.customerService.getCustomers(data.selectedInstitution, data.livingArea);
      }),
      map(residents => {
        return residents?.map((resident: Resident): SelectOption<UUID> => {
          return { label: resident.full_name, value: resident.id };
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
    this.residentOptions = options;
    this.changeDetectorRef.markForCheck();
  }
}
