import { Component, input, output, ChangeDetectionStrategy } from '@angular/core';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'app-search-bar',
  standalone: true,
  templateUrl: './search.html',
  styleUrl: './search.css',
})
export class Search {
  readonly placeholder = input<string>('Search...');
  readonly searchChange = output<string>();

  onInput(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.searchChange.emit(value);
  }
}
