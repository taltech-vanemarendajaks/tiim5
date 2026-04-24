import { Injectable, signal } from '@angular/core';
import ee from '../../../assets/i18n/ee.json';

export type Locale = 'ee';

interface TranslationTree {
  [key: string]: string | TranslationTree;
}

const DICTIONARIES: Record<Locale, TranslationTree> = { ee };

@Injectable({ providedIn: 'root' })
export class I18nService {
  readonly locale = signal<Locale>('ee');

  translate(key: string): string {
    const parts = key.split('.');
    let node: string | TranslationTree = DICTIONARIES[this.locale()];
    for (const part of parts) {
      if (typeof node !== 'object' || !Object.prototype.hasOwnProperty.call(node, part)) {
        return key;
      }
      node = node[part];
    }
    return typeof node === 'string' ? node : key;
  }
}
