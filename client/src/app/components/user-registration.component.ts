import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TokenService } from '../services/token.service';

@Component({
  selector: 'app-user-registration',
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.css']
})
export class UserRegistrationComponent implements OnInit {

  newUserForm!: FormGroup
  token!: string

  constructor(
    private tokenService: TokenService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.newUserForm = this.createForm();
    this.token = this.tokenService.token
  }

  createForm(): FormGroup {
    return this.fb.group({
      username: this.fb.control('', [Validators.required]),
      password: this.fb.control('', [Validators.required])
    })
  }
}
