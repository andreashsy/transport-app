import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
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
  notifications!: NotificationForm[]

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private tokenService: TokenService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.addNotificationForm = this.createForm()
    this.initialiseNotifications()
  }

  createForm() {
    return this.fb.group({
      busStopCode: this.fb.control('', [Validators.required, Validators.minLength(5), Validators.maxLength(5)]),
      time: this.fb.control('', [Validators.required]),
      dayOfWeek: this.fb.control('', [Validators.required])
    })
  }

  async addNotification() {
    let notification = this.addNotificationForm.value as NotificationForm
    notification.username = this.tokenService.username || ""
    console.info("Sending notification...", notification)
    await this.userService.addNotification(notification)
      .then(result => {
        this.initialiseNotifications()
      })
      .catch(err => {
        console.error(err)
      })
  }

  async initialiseNotifications() {
    if (this.tokenService.jwtToken) {
    await this.userService.getNotifications(this.tokenService.username)
      .then(result => {
        console.info(result)
        this.notifications = result.notifications as NotificationForm[]
      })
      .catch(err => {
        console.error(JSON.stringify(err))
      })
    } else {
      alert("Please login first!")
      this.router.navigate(['/'])
    }
  }

  async deleteNotification(index: number) {
    console.log("deleting notification index: ", index)
    await this.userService.deleteNotification(
      this.tokenService.username,
      this.notifications[index].busStopCode,
      this.notifications[index].cronString)
      .then(result => {
        console.info(result)
        this.notifications.splice(index, 1)
      })
      .catch(err => {
        console.error(err)
      })
  }

}
