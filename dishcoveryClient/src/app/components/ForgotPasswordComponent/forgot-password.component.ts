import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../../service/UserService';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-forgot-password',
  standalone: false,
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent {
  private fb = inject(FormBuilder)
  private userSvc = inject(UserService)
  private forgetPasswordSub!: Subscription

  protected form!: FormGroup
  protected success! : string | null
  protected error!: string | null


  ngOnInit():void {

    this.form = this.createForm()

  }

  createForm():FormGroup {
    return this.fb.group({
      email: this.fb.control<string>("", [Validators.required, Validators.email])
    })
  }


  processForm(): void {
    const email = this.form.controls["email"]?.value;
  
    console.info("Your email that you want to reset is ", email);
  
    // Reset previous messages
    this.success = null;
    this.error = null;
  
    // Send request to service
    this.forgetPasswordSub = this.userSvc.forgotPassword(email).subscribe({
      next: (response) => {
        console.info("This is your response ", response);
        this.success = response.Message;
  
        // Delay form reset so user sees success message
        setTimeout(() => {
          this.form.reset();
          this.success = null; // Remove success message after reset
        }, 2000); // Display success for 2 seconds before resetting
      },
      error: (error) => {
        console.info("This is your error", error);
        this.error = error;
  
        // Ensure success message is cleared if there was an error
        this.success = null;
      },
    });
  }
  

   // Validation 
  isCtrlInvalid(ctrlName: string):boolean {
    return !!this.form.get(ctrlName)?.invalid
}
}
