import { TestBed } from '@angular/core/testing';

import { PartCreatorService } from './part-creator.service';

describe('PartCreatorService', () => {
  let service: PartCreatorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PartCreatorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
