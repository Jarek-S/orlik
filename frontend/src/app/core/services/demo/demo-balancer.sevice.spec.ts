import {TestBed} from '@angular/core/testing';

import {DemoBalancerService} from './demo-balancer.service';

describe('DemoBalancerService', () => {
  let service: DemoBalancerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DemoBalancerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
