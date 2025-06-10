import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() { }

  setToken(token:string){
    localStorage.setItem("Token", token)
  }

  getToken(){
    return localStorage.getItem("Token")
  }

  deleteToken(){
    return localStorage
  }

}
