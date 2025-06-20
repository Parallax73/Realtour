import { Component, OnInit, inject, PLATFORM_ID, ViewChild } from '@angular/core';
import { MenubarModule } from 'primeng/menubar';
import { MenuItem } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { isPlatformBrowser } from '@angular/common';
import { SplitButtonModule } from 'primeng/splitbutton';
import { RouterService } from '../../services/router/router.service';
import { Popover, PopoverModule } from 'primeng/popover';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-menu-navbar',
  standalone: true,
  imports: [
    MenubarModule,
    ButtonModule,
    SplitButtonModule,
    PopoverModule
  ],
  templateUrl: './menu-navbar.component.html',
  styleUrls: ['./menu-navbar.component.scss']
})
export class MenuNavbarComponent implements OnInit {

  @ViewChild('popover') popover!: Popover;
  routerService = inject(RouterService);
  authService = inject(AuthService)
  private readonly platformId = inject(PLATFORM_ID);
  
  items: MenuItem[] | undefined;
  visible: boolean = false;
  isDarkMode = false;
  useritems: MenuItem[] | undefined;

  showDialog() {
    this.visible = true;
  }

  private updateThemeClass(isDark: boolean): void {
    if (isPlatformBrowser(this.platformId)) {
      const element = document.documentElement;
      if (element) {
        if (isDark) {
          element.classList.add('my-app-dark');
        } else {
          element.classList.remove('my-app-dark');
        }
      }
    }
  }

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      const savedTheme = localStorage.getItem('theme');
      if (savedTheme) {
        this.isDarkMode = savedTheme === 'dark';
        this.updateThemeClass(this.isDarkMode);
      } else {
        const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
        this.isDarkMode = prefersDark;
        this.updateThemeClass(this.isDarkMode);
      }
    }

    this.useritems = [
      {
        label: 'Login',
        command: () => {
          this.routerService.navigateToLogin();
        }
      },
      {
        label: 'Register',
        command: () => {
          this.routerService.navigateToRegister();
        }
      }
    ];

    this.items = [
      {
        label: 'Home',
        command: () => {
          this.routerService.navigateToHome();
        }
      },
      {
        label: 'Units',
        command: () => {
          this.routerService.navigateToUnits();
        }
      },
      {
        label: 'Realtors',
        command: () => {
          this.routerService.navigateToRealtors();
        }
      },
      {
        label: 'All pages',
        items: [
          {
            label: 'Code',
            command: () => {
              this.routerService.navigateToCode();
            }
          },
          {
            label: 'Map',
            command: () => {
              this.routerService.navigateToMap();
            }
          },
          {
            label: 'Contact',
            command: () => {
              this.routerService.navigateToContact();
            }
          },
          {
            label: 'Models',
            command: () => {
              this.routerService.navigateToModels();
            }
          },
          {
            label: 'Model Editor',
            command: () => {
              this.routerService.navigateToEditor();
            }
          }
        ]
      }
    ];
  }

  toggleDarkMode() {
    if (isPlatformBrowser(this.platformId)) {
      this.isDarkMode = !this.isDarkMode;
      this.updateThemeClass(this.isDarkMode);
      localStorage.setItem('theme', this.isDarkMode ? 'dark' : 'light');
    }
  }

  toggleUser(){
    if(this.authService.isTokenPresent()){
      this.popover.toggle(event);
    } else{
    this.routerService.navigateToLogin();
    }  
  }

  logout(){
    this.authService.deleteToken();
    window.location.reload();
  }
}
