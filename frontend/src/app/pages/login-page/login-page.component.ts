import { Component, ViewEncapsulation } from '@angular/core';
import { SelectButtonModule } from 'primeng/selectbutton';
import { ButtonModule } from 'primeng/button';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MenuNavbarComponent } from "../../components/menu-navbar/menu-navbar.component";
import { SelectModule } from 'primeng/select';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { BrazilianStates, States } from '../../utils/enums/states';
import { UserService } from '../../services/user/user.service'; 

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [
    SelectButtonModule,
    ButtonModule,
    FormsModule,
    CommonModule,
    MenuNavbarComponent,
    SelectModule,
    InputGroupAddonModule,
    InputGroupModule
  ],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class LoginPageComponent {
  optionToggle = true;
  selectedMode: string = 'Register';
  selectedClass: string = 'client';
  email: string = '';
  password: string = '';
  username: string = '';
  creci: string = '';
  isEmailValid: boolean = true;
  isSubmitted: boolean = false;
  showPasswordFields: boolean = false;
  isRealtor = false;
  states: States[] = BrazilianStates;
  selectedState: string = '';
 
   constructor(private userService: UserService) {}

  stateOptions: any[] = [
    { label: 'Register' },
    { label: 'Login' }
  ];

  classOptions: any[] = [
    { label: 'Client', value: 'client' },
    { label: 'Realtor', value: 'realtor'}
  ];

  toggle() {
    this.email = '';
    this.password = '';
    this.username = '';
    this.isSubmitted = false;
    this.showPasswordFields = false;
    this.isEmailValid = true;
    this.optionToggle = this.selectedMode === 'Register';
  }

  validateEmail() {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    this.isEmailValid = emailRegex.test(this.email);
  }

  onSignupClick() {
  this.isSubmitted = true;
  this.validateEmail();
  
  if (!this.isEmailValid) {
    return;
  }

  if (!this.showPasswordFields) {
   
    this.showPasswordFields = true;
  } else {
  
    if (this.password && this.username) { 
      this.registerClient();
    }
  }
}

  onLoginClick() {
    this.isSubmitted = true;
    this.validateEmail();
    if (this.isEmailValid) {
    }
  }

  changeClass() {
    this.isRealtor = this.selectedClass === 'realtor';
    console.log('isRealtor:', this.isRealtor);
  }

  private registerClient() {
    const client = {
      email: this.email,
      username: this.username,
      password: this.password,
      savedUnits: []
    };

  

     this.userService.createClient(client).subscribe({
       next: (token: string) => {
             console.log('Registration successful, token received:', token);
        },
        error: (error: unknown) => {
            if (error instanceof Error) {
                 console.error('Registration failed:', error.message);
            } else {
                console.error('Registration failed:', error);
             }
            
        }
     });
   }
}