import { TestBed } from '@angular/core/testing';
import { Component, ChangeDetectionStrategy } from '@angular/core';
import { NotificationComponent } from './notification.component';
import { it, expect } from 'vitest';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
  imports: [NotificationComponent],
  template: `<app-notification [message]="message" />`,
})
class TestHostComponent {
  message = 'Test message';
}

it('should display the provided message', () => {
  const fixture = TestBed.createComponent(TestHostComponent);
  fixture.detectChanges();

  const el = fixture.nativeElement.querySelector('.notification');
  expect(el.textContent.trim()).toBe('Test message');
});

it('should display default message when none provided', () => {
  @Component({
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: true,
    imports: [NotificationComponent],
    template: `<app-notification />`,
  })
  class NoInputHostComponent {}

  TestBed.overrideComponent(NoInputHostComponent, {});
  const fixture = TestBed.createComponent(NoInputHostComponent);
  fixture.detectChanges();

  const el = fixture.nativeElement.querySelector('.notification');
  expect(el.textContent.trim()).toBe('No results found.');
});
