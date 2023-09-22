import { ChangeDetectionStrategy, Component, Input, forwardRef } from '@angular/core';
import { ControlValueAccessor, FormControl, NG_VALUE_ACCESSOR, ReactiveFormsModule } from '@angular/forms';
import { NgbDateAdapter, NgbDateParserFormatter, NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';

import { CommonModule } from '@angular/common';
import { DateParserFormatter } from '@framework/utilities/date-parser-formatter';
import { DateTime } from 'luxon';
import { IsoDateAdapter } from '@framework/utilities/iso-date-adapter';

@UntilDestroy()
@Component({
  selector: 'app-filter-date',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NgbDatepickerModule],
  templateUrl: './filter-date.component.html',
  styleUrls: ['./filter-date.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => FilterDateComponent),
      multi: true
    },
    { provide: NgbDateAdapter, useClass: IsoDateAdapter },
    { provide: NgbDateParserFormatter, useClass: DateParserFormatter },
  ],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FilterDateComponent implements ControlValueAccessor {
  /** Placeholder text for input field. */
  @Input() public placeholder = 'Datum wählen';
  /** Input is readonly. Default is `true`. */
  @Input() public readonly: boolean = true;
  /** Minium date to select. */
  @Input() public minDate: DateTime | undefined;
  /** Maximum date to select. */
  @Input() public maxDate: DateTime | undefined;
  /** ID to associate the label with the control. */
  @Input() labelForId: string | undefined;

  /** Date form control */
  public readonly date = new FormControl<string | string[] | null>(null);

  public onTouched: () => void = () => { };

  writeValue(val: any): void {
    this.date.setValue(val, { emitEvent: false });
  }

  registerOnChange(fn: any): void {
    this.date.valueChanges.pipe(
      untilDestroyed(this)
    ).subscribe(fn);
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    isDisabled ? this.date.disable() : this.date.enable();
  }
}
