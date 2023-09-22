import { ComponentFixture, TestBed } from '@angular/core/testing';
import { mockCustomerService, mockGlobalInstitutionService } from 'testing';

import { CustomerService } from '@framework/services/customer/customer.service';
import { FilterResidentComponent } from './filter-resident.component';
import { GlobalInstitutionService } from '@framework/services/global-institution';

describe('FilterResidentComponent', () => {
  let component: FilterResidentComponent;
  let fixture: ComponentFixture<FilterResidentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        FilterResidentComponent,
      ],
      providers: [
        { provide: GlobalInstitutionService, useValue: mockGlobalInstitutionService },
        { provide: CustomerService, useValue: mockCustomerService },
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FilterResidentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
