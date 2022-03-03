import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NotificationForm } from '../models/model';
import { TokenService } from '../services/token.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-user-notifications',
  templateUrl: './user-notifications.component.html',
  styleUrls: ['./user-notifications.component.css']
})
export class UserNotificationsComponent implements OnInit {

  addNotificationForm!: FormGroup

  constructor(
    private fb: FormBuilder,
    private tokenService: TokenService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.addNotificationForm = this.createForm()
  }

  createForm() {
    return this.fb.group({
      busStopCode: this.fb.control('', [Validators.required]),
      time: this.fb.control('', [Validators.required]),
      dayOfWeek: this.fb.control('', [Validators.required])
    })
  }

  async addNotification() {
    let notification = this.addNotificationForm.value as NotificationForm
    notification.username = this.tokenService.username || ""
    console.info("Sending notification...", notification)
    await this.userService.addNotification(notification)
  }

}
