import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RentEditComponent } from './rent-edit.component';

describe('RentEditComponent', () => {
  let component: RentEditComponent;
  let fixture: ComponentFixture<RentEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RentEditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RentEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
