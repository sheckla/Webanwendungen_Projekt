import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR, ReactiveFormsModule } from '@angular/forms';
import { NgbCalendar, NgbDate, NgbDateParserFormatter, NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';

import { CommonModule } from '@angular/common';
import { DateParserFormatter } from '@framework/utilities/date-parser-formatter';
import { DateTime } from 'luxon';

export class DateRange {
  from: DateTime;
  to: DateTime;

  constructor(from: DateTime, to: DateTime) {
    this.from = from;
    this.to = to;
  }
}

@Component({
  selector: 'app-filter-date-range',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NgbDatepickerModule],
  templateUrl: './filter-date-range.component.html',
  styleUrls: ['./filter-date-range.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => FilterDateRangeComponent),
      multi: true
    },
    { provide: NgbDateParserFormatter, useClass: DateParserFormatter },
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FilterDateRangeComponent implements ControlValueAccessor {
  /** Minimum selectable date. */
  @Input() public minDate: DateTime | undefined;
  /** Maximum selectable date. */
  @Input() public maxDate: DateTime | undefined;

  /** Placeholder for "from" input. */
  @Input() public placeholderFrom: string = 'Von';
  /** Placeholder for "to" input. */
  @Input() public placeholderTo: string = 'Bis';

  /** ID to associate the label with the control. */
  @Input() labelForId: string | undefined;

  /** The current hovered date */
  public hoveredDate: NgbDate | null = null;

  public fromDate: NgbDate | null;
  public toDate: NgbDate | null;

  private touched: boolean = false;
  public disabled: boolean = false;

  private onChange: (value: DateRange) => {};
  private onTouched: () => {};

  constructor(
    private calendar: NgbCalendar,
    public formatter: NgbDateParserFormatter,
    private changeDetectorRef: ChangeDetectorRef,
  ) { }

  writeValue(val: DateRange): void {
    this.fromDate = val ? new NgbDate(val.from.year, val.from.month, val.from.day) : null;
    this.toDate = val ? new NgbDate(val.to.year, val.to.month, val.to.day) : null;
    this.changeDetectorRef.detectChanges();
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    this.disabled = isDisabled;
    this.changeDetectorRef.detectChanges();
  }

  public onDateSelection(date: NgbDate): void {
    this.markAsTouched();

    if (!this.fromDate && !this.toDate) {
      this.fromDate = date;
    } else if (this.fromDate && !this.toDate && date && date.after(this.fromDate)) {
      this.toDate = date;
      const from = DateTime.fromObject({ year: this.fromDate.year, month: this.fromDate.month, day: this.fromDate.day });
      const to = DateTime.fromObject({ year: this.toDate.year, month: this.toDate.month, day: this.toDate.day });
      this.onChange(new DateRange(from, to));
    } else {
      this.toDate = null;
      this.fromDate = date;
    }
  }

  public isHovered(date: NgbDate): boolean {
    return (
      this.fromDate && !this.toDate && this.hoveredDate && date.after(this.fromDate) && date.before(this.hoveredDate)
    );
  }

  public isInside(date: NgbDate): boolean {
    return this.toDate && date.after(this.fromDate) && date.before(this.toDate);
  }

  public isRange(date: NgbDate): boolean {
    return (
      date.equals(this.fromDate) ||
      (this.toDate && date.equals(this.toDate)) ||
      this.isInside(date) ||
      this.isHovered(date)
    );
  }

  public validateInput(currentValue: NgbDate | null, input: string): NgbDate | null {
    const parsed = this.formatter.parse(input);
    return parsed && this.calendar.isValid(NgbDate.from(parsed)) ? NgbDate.from(parsed) : currentValue;
  }

  private markAsTouched(): void {
    if (!this.touched) {
      this.onTouched();
      this.touched = true;
    }
  }
}
