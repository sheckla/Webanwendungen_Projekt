import { ChangeDetectionStrategy, Component, Input, forwardRef } from '@angular/core';
import { ControlValueAccessor, FormControl, NG_VALUE_ACCESSOR, ReactiveFormsModule } from '@angular/forms';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';

import { CommonModule } from '@angular/common';
import { debounceTime } from 'rxjs';

@UntilDestroy()
@Component({
  selector: 'app-filter-search',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './filter-search.component.html',
  styleUrls: ['./filter-search.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => FilterSearchComponent),
      multi: true
    }
  ],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FilterSearchComponent implements ControlValueAccessor {
  /** ID to associate the label with the control. */
  @Input() labelForId: string | undefined;
  /** Debounce time of the search input. Default is 300. */
  @Input() debounceTime: number = 300;

  /** Search form control */
  public readonly search = new FormControl<string | null>(null);

  public onTouched: () => void = () => { };

  writeValue(val: any): void {
    this.search.setValue(val, { emitEvent: false });
  }

  registerOnChange(fn: any): void {
    this.search.valueChanges.pipe(
      debounceTime(this.debounceTime),
      untilDestroyed(this)
    ).subscribe(fn);
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    isDisabled ? this.search.disable() : this.search.enable();
  }
}
