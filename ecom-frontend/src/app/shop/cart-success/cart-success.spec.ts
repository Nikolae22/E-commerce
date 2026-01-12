import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CartSuccess } from './cart-success';

describe('CartSuccess', () => {
  let component: CartSuccess;
  let fixture: ComponentFixture<CartSuccess>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CartSuccess]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CartSuccess);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
