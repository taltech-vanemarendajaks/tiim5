import { Injectable, signal, inject } from '@angular/core';
import { UserResponse, UserService } from '@/client';
import { map, tap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class UserStore {
  private readonly userService = inject(UserService);
  private readonly _user = signal<UserResponse | null>(null);

  readonly user = this._user.asReadonly();

  loadUser() {
    return this.userService.getAllUsers().pipe(
      map((users) => users[0] ?? null),
      tap((user) => this._user.set(user)),
    ); //ToDo - should be changed to correct signed in user
  }
}
