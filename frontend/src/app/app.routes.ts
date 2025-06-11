import { Routes } from '@angular/router';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { ChatPageComponent } from './pages/chat-page/chat-page.component';

export const routes: Routes = [
    {path: '', component: HomePageComponent},
    {path: 'login', component: LoginPageComponent},
    {path: 'chat', component: ChatPageComponent}
];
