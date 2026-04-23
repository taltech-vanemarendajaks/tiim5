import { TestBed } from '@angular/core/testing';
import { Component, ChangeDetectionStrategy } from '@angular/core';
import { SearchComponent } from './search.component';
import { it, expect } from 'vitest';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
  imports: [SearchComponent],
  template: `<app-search-bar [placeholder]="placeholder" (searchChange)="onSearch($event)" />`,
})
class TestHostComponent {
  placeholder = 'Search...';
  lastValue = '';
  onSearch(value: string) {
    this.lastValue = value;
  }
}

it('should render input with correct placeholder', () => {
  const fixture = TestBed.createComponent(TestHostComponent);
  fixture.detectChanges();

  const input = fixture.nativeElement.querySelector('input');
  expect(input.placeholder).toBe('Search...');
});

it('should emit searchChange when user types', () => {
  const fixture = TestBed.createComponent(TestHostComponent);
  fixture.detectChanges();

  const input = fixture.nativeElement.querySelector('input');
  input.value = 'hello';
  input.dispatchEvent(new Event('input'));
  fixture.detectChanges();

  expect(fixture.componentInstance.lastValue).toBe('hello');
});

it('should render custom placeholder', () => {
  const fixture = TestBed.createComponent(TestHostComponent);
  fixture.componentInstance.placeholder = 'Otsi nime järgi...';
  fixture.detectChanges();

  const input = fixture.nativeElement.querySelector('input');
  expect(input.placeholder).toBe('Otsi nime järgi...');
});

it('should render search icon', () => {
  const fixture = TestBed.createComponent(TestHostComponent);
  fixture.detectChanges();

  const icon = fixture.nativeElement.querySelector('.search-icon');
  expect(icon).not.toBeNull();
});
