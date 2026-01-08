import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductsFilter } from './products-filter';

describe('ProductsFilter', () => {
  let component: ProductsFilter;
  let fixture: ComponentFixture<ProductsFilter>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductsFilter]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductsFilter);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
