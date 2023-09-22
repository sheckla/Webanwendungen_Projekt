import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterInstitutionComponent } from './filter-institution.component';
import { GlobalInstitutionService } from '@framework/services/global-institution';
import { mockGlobalInstitutionService } from 'testing';

describe('FilterInstitutionComponent', () => {
  let component: FilterInstitutionComponent;
  let fixture: ComponentFixture<FilterInstitutionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        FilterInstitutionComponent,
      ],
      providers: [
        { provide: GlobalInstitutionService, useValue: mockGlobalInstitutionService },
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FilterInstitutionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
