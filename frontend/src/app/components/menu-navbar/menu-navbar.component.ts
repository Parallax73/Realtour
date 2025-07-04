import { Component, OnInit, inject, PLATFORM_ID, ViewChild } from '@angular/core';
import { MenubarModule } from 'primeng/menubar';
import { MenuItem } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { isPlatformBrowser } from '@angular/common';
import { SplitButtonModule } from 'primeng/splitbutton';
import { RouterService } from '../../services/router/router.service';
import { Popover, PopoverModule } from 'primeng/popover';
import { AuthService } from '../../services/auth/auth.service';
import { TranslateService, TranslateModule } from '@ngx-translate/core';
import { TooltipModule } from 'primeng/tooltip';

@Component({
  selector: 'app-menu-navbar',
  standalone: true,
  imports: [
    MenubarModule,
    ButtonModule,
    SplitButtonModule,
    PopoverModule,
    TranslateModule,
    TooltipModule
  ],
  templateUrl: './menu-navbar.component.html',
  styleUrls: ['./menu-navbar.component.scss']
})
export class MenuNavbarComponent implements OnInit {
  @ViewChild('popover') popover!: Popover;
  routerService = inject(RouterService);
  authService = inject(AuthService);
  translate = inject(TranslateService);
  private readonly platformId = inject(PLATFORM_ID);

  items: MenuItem[] | undefined;
  visible: boolean = false;
  isDarkMode = false;
  useritems: MenuItem[] | undefined;
  currentLang: string = 'en';

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
    const savedLang = localStorage.getItem('lang') || 'en';
    this.currentLang = savedLang;
    this.translate.setDefaultLang('en');
    this.translate.use(this.currentLang).subscribe(() => {
      this.initializeMenu();
    });

    if (isPlatformBrowser(this.platformId)) {
      const savedTheme = localStorage.getItem('theme');
      if (savedTheme) {
        this.isDarkMode = savedTheme === 'dark';
        this.updateThemeClass(this.isDarkMode);
      }
    }
  }

  switchLanguage() {
    this.currentLang = this.currentLang === 'en' ? 'pt' : 'en';
    this.translate.use(this.currentLang).subscribe(() => {
      localStorage.setItem('lang', this.currentLang);
      this.initializeMenu();
    });
  }

  private initializeMenu() {
    this.translate.get([
      'USER.LOGIN',
      'USER.REGISTER',
      'MENU.HOME',
      'MENU.UNITS',
      'MENU.REALTORS',
      'MENU.ALL_PAGES',
      'MENU.CHAT',
      'MENU.MAP',
      'MENU.CONTACT_US'
    ]).subscribe(translations => {
      this.useritems = [
        {
          label: translations['USER.LOGIN'],
          command: () => {
            this.routerService.navigateToLogin();
          }
        },
        {
          label: translations['USER.REGISTER'],
          command: () => {
            this.routerService.navigateToRegister();
          }
        }
      ];

      this.items = [
        {
          label: translations['MENU.HOME'],
          command: () => {
            this.routerService.navigateToHome();
          }
        },
        {
          label: translations['MENU.UNITS'],
          command: () => {
            this.routerService.navigateToUnits();
          }
        },
        {
          label: translations['MENU.REALTORS'],
          command: () => {
            this.routerService.navigateToRealtors();
          }
        },
        {
          label: translations['MENU.ALL_PAGES'],
          items: [
            {
              label: translations['MENU.CHAT'],
              command: () => {
                this.routerService.navigateToChat();
              }
            },
            {
              label: translations['MENU.MAP'],
              command: () => {
                this.routerService.navigateToMap();
              }
            },
            {
              label: translations['MENU.CONTACT_US'],
              command: () => {
                this.routerService.navigateToContact();
              }
            }
          ]
        }
      ];
    });
  }

  toggleDarkMode() {
    if (isPlatformBrowser(this.platformId)) {
      this.isDarkMode = !this.isDarkMode;
      this.updateThemeClass(this.isDarkMode);
      localStorage.setItem('theme', this.isDarkMode ? 'dark' : 'light');
    }
  }

  toggleUser() {
    if (this.authService.isTokenPresent()) {
      console.log("Called");
      this.popover.toggle(event);
    } else {
      console.log("Notcalled");
      this.routerService.navigateToLogin();
    }
  }

  logout() {
    this.authService.deleteToken();
    window.location.reload();
  }
}
