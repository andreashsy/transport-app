import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from '../models/model';
import { TokenService } from '../services/token.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit {

  loginForm!: FormGroup

  constructor(
    private http: HttpClient,
    private fb: FormBuilder,
    private router: Router,
    private userService: UserService,
    private tokenService: TokenService
  ) { }

  ngOnInit(): void {
    this.loginForm = this.createForm()
  }

  createForm(): FormGroup {
    return this.fb.group({
      username: this.fb.control('', [Validators.required]),
      password: this.fb.control('', [Validators.required])
    })
  }

  async login() {
    let user = this.loginForm.value as User
    await this.userService.login(user)
      .then(result => {
        console.info(result)
        this.tokenService.setJwtToken(result.token)
        this.tokenService.username = user.username
        console.info("Logged in, JWT Token set! ", this.tokenService.jwtToken)
        this.router.navigate(['/'])
      })
      .catch(err =>{
        console.error(err)
      })
  }

}
