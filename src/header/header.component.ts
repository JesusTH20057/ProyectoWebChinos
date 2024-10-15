// header.component.ts
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  imports: [RouterLink], 
  styleUrls: ['./header.component.css'],

  standalone: true
})
export class HeaderComponent {
}
