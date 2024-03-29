import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchContainerComponent } from './search-container.component';

describe('SearchContainerComponent', () => {
  let component: SearchContainerComponent;
  let fixture: ComponentFixture<SearchContainerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SearchContainerComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
    });
    fixture = TestBed.createComponent(SearchContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
