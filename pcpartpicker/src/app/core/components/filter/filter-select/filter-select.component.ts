import {
  ChangeDetectionStrategy,
  Component,
  Input,
  forwardRef,
} from '@angular/core';
import {
  ControlValueAccessor,
  FormControl,
  NG_VALUE_ACCESSOR,
  ReactiveFormsModule,
} from '@angular/forms';
// import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';

import { CommonModule } from '@angular/common';
import { NgSelectModule } from '@ng-select/ng-select';

export type SelectOption<T = string | number> = {
  value: T;
  label: string;
};

// @UntilDestroy()
@Component({
  selector: 'app-filter-select',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NgSelectModule],
  templateUrl: './filter-select.component.html',
  styleUrls: ['./filter-select.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => FilterSelectComponent),
      multi: true,
    },
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FilterSelectComponent implements ControlValueAccessor {
  /** Array with available select options. */
  @Input() options!: SelectOption[] | null;
  /** Placeholder for select, if no options is selected. */
  @Input() placeholder: string = '';
  /** Allow multiple selection. Default is `false`. */
  @Input() multiple: boolean = false;
  /** Selected option can be cleared. Default is `true`. */
  @Input() clearable: boolean = true;
  /** Text that is displayed if no options are available. */
  @Input() notFoundText: string = '';
  /** ID to associate the label with the control. */
  @Input() labelForId: string | undefined;

  /** Select form control */
  public readonly select = new FormControl<string | string[] | null>(null);

  public onTouched: () => void = () => {};

  writeValue(val: any): void {
    this.select.setValue(val, { emitEvent: false });
  }

  registerOnChange(fn: any): void {
    this.select.valueChanges
      .pipe
      // untilDestroyed(this)
      ()
      .subscribe(fn);
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    isDisabled ? this.select.disable() : this.select.enable();
  }
}
