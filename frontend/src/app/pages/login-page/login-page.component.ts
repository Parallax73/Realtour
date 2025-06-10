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
import { Client } from '../../utils/models/client';
import { Realtor } from '../../utils/models/realtor';
import { RouterService } from '../../services/router/router.service';

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
  selectedState: States | null = null;
  errorMessage: string = '';
 
  constructor(private userService: UserService, private router: RouterService) {}

  stateOptions: any[] = [
    { label: 'Register' },
    { label: 'Login' }
  ];

  classOptions: any[] = [
    { label: 'Client', value: 'client' },
    { label: 'Realtor', value: 'realtor'}
  ];

  formatCreci(event: any) {
    const input = event.target as HTMLInputElement;
    input.value = input.value.replace(/\D/g, '').substring(0, 6);
    this.creci = input.value;
  }

  toggle() {
    this.email = '';
    this.password = '';
    this.username = '';
    this.creci = '';
    this.selectedState = null;
    this.isSubmitted = false;
    this.showPasswordFields = false;
    this.isEmailValid = true;
    this.optionToggle = this.selectedMode === 'Register';
    this.errorMessage = '';
  }

  validateEmail() {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    this.isEmailValid = emailRegex.test(this.email);
  }

  validateFields(): boolean {
    if (!this.email || !this.password || !this.username) {
      this.errorMessage = 'Please fill in all required fields';
      return false;
    }

    if (this.username.length < 4 || this.password.length < 4) {
      this.errorMessage = 'Username and password must be at least 4 characters';
      return false;
    }

    return true;
  }

  validateCreci(): boolean {
    if (!this.selectedState || !this.creci) {
      this.errorMessage = 'Please select a state and enter CRECI number';
      return false;
    }

    const numericCreci = this.creci.replace(/\D/g, '');

    if (numericCreci.length !== 6) {
      this.errorMessage = 'CRECI number must be exactly 6 digits';
      return false;
    }

    if (!/^\d+$/.test(numericCreci)) {
      this.errorMessage = 'CRECI number must contain only digits';
      return false;
    }

    return true;
  }

  onSignupClick() {
    this.isSubmitted = true;
    this.validateEmail();
    
    if (!this.isEmailValid) {
      this.errorMessage = 'Please enter a valid email';
      return;
    }

    if (!this.showPasswordFields) {
      this.showPasswordFields = true;
      this.errorMessage = '';
      return;
    }

    if (!this.validateFields()) {
      return;
    }

    if (this.isRealtor) {
      if (this.validateCreci()) {
        this.registerRealtor();
      }
    } else {
      this.registerClient();
    }
  }

 onLoginClick() {
    this.isSubmitted = true;
    this.validateEmail();

    if (this.isEmailValid) {
      this.userService.loginClient(this.email, this.password).subscribe({
        next: (success: boolean) => {
          if (success) {
            this.router.navigateToHome(); 
          }
        },
        error: () => {
          alert('Invalid login credentials.');
        }
      });
    }
  }
  changeClass() {
    this.isRealtor = this.selectedClass === 'realtor';
    if (!this.isRealtor) {
      this.creci = '';
      this.selectedState = null;
      this.errorMessage = '';
    }
  }

  private registerClient() {
    const client: Client = {
      email: this.email,
      username: this.username,
      password: this.password
    };

    this.userService.createClient(client).subscribe({
      next: (response: string) => {
        if (response) {
          this.selectedMode = 'Login';
          this.toggle();
        }
      },
      error: (errorMessage: string) => {
        this.errorMessage = errorMessage;
      }
    });
  }

  private registerRealtor() {
    const formattedCreci = `${this.selectedState}-${this.creci.replace(/\D/g, '')}`;

    const realtor: Realtor = {
      email: this.email,
      username: this.username,
      password: this.password,
      creci: formattedCreci
    };
    
    this.userService.createRealtor(realtor).subscribe({
      next: (response: string) => {
        if (response) {
          this.selectedMode = 'Login';
          this.toggle();
        }
      },
      error: (errorMessage: string) => {
        this.errorMessage = errorMessage;
      }
    });
  }

 

}