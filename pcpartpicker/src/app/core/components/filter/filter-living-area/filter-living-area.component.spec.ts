import { ComponentFixture, TestBed } from '@angular/core/testing';
import { mockGlobalInstitutionService, mockLivingArea } from 'testing';

import { FilterLivingAreaComponent } from './filter-living-area.component';
import { GlobalInstitutionService } from '@framework/services/global-institution';
import { LivingAreaService } from '@framework/services/living-area/living-area.service';
import { of } from 'rxjs';

describe('FilterLivingAreaComponent', () => {
  let component: FilterLivingAreaComponent;
  let fixture: ComponentFixture<FilterLivingAreaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        FilterLivingAreaComponent,
      ],
      providers: [
        { provide: GlobalInstitutionService, useValue: mockGlobalInstitutionService },
        { provide: LivingAreaService, useValue: jasmine.createSpyObj<LivingAreaService>({ getLivingAreas: of([mockLivingArea]) }) },
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FilterLivingAreaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
