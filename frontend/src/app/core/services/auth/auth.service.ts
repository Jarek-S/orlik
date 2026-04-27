import {Injectable, signal} from '@angular/core';

@Injectable({providedIn: 'root'})
export class AuthService {
  // Symulacja stanu zalogowania
  private _isLoggedIn = signal(false);
  isLoggedIn = this._isLoggedIn.asReadonly();

  user = signal({name: 'Trener DemoComponent'});

  login() {
    this._isLoggedIn.set(true);
  }

  logout() {
    this._isLoggedIn.set(false);
  }
}
