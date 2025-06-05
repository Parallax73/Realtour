import { Component, OnInit, inject, PLATFORM_ID } from '@angular/core';
import { MenubarModule } from 'primeng/menubar';
import { MenuItem } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { isPlatformBrowser } from '@angular/common';
import { SplitButtonModule } from 'primeng/splitbutton';
import { RouterService } from '../../services/router.service';


@Component({
  selector: 'app-menu-navbar',
  standalone: true,
  imports: [
    MenubarModule,
    ButtonModule,
    SplitButtonModule
  ],
  templateUrl: './menu-navbar.component.html',
  styleUrls: ['./menu-navbar.component.scss']
})
export class MenuNavbarComponent implements OnInit {

    
    routerService = inject(RouterService);
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


        this.items = [
            {
                label: 'Home'
            },
            {
                label: 'Units'
            },
            {
              label: 'Realtors'
            },
            {
                label: 'All pages',
                items: [
                    {
                        label: 'Code'
                    },
                    {
                      label: 'Map'
                    },
                    {
                        label: 'Contact'
                    },
                    {
                        label: 'Models'
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
}