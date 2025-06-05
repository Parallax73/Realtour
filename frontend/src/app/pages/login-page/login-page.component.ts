import { Component } from '@angular/core';
import { SelectButtonModule } from 'primeng/selectbutton';
import { FloatLabelModule } from 'primeng/floatlabel';
import { ButtonModule } from 'primeng/button';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login-page',
  imports: [
    FloatLabelModule,
    SelectButtonModule,
    ButtonModule,
    FormsModule
    
  ],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss'
})
export class LoginPageComponent {

selectedState: string = 'Register'; // Add this line to set default value
    stateOptions: any[] = [
        { label: 'Register' },
        { label: 'Login' }
    ];

  

}
