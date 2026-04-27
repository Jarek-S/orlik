import {Component, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css',
  standalone: true
})
export class App implements OnInit {


  ngOnInit() {

    // if (!this.kc.authenticated) {
    //   this.kc.login({
    //     // redirectUri: window.location.origin
    //   });
    // }
  }
}
