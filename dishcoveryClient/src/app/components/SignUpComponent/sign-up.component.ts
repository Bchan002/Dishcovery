import { Component, inject } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { UserService } from '../../../service/UserService';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { SignUpDetails } from '../../model/models';

@Component({
  selector: 'app-sign-up',
  standalone: false,
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.css'
})
export class SignUpComponent {
    //Inject FormBuilder 
    private fb = inject(FormBuilder)
    private userSvc = inject(UserService)
    private router = inject(Router)
    private sub!: Subscription
    protected hidePassword = true; // For toggling password visibility

    //Create a formGroup 
    signUpForm!: FormGroup
    protected errorMessage!: String

    ngOnInit(): void {
        this.signUpForm = this.createForm()
    }
    

    /*
     * Create form 
     */


    createForm():FormGroup{
      return this.fb.group({
        username: this.fb.control<string>('', [Validators.required, Validators.minLength(5)]),
        email: this.fb.control<string>('', [Validators.required, Validators.email]),
        password: this.fb.control<string>('', [Validators.required, Validators.minLength(5)]),
        confirmPassword: this.fb.control<string>('', [Validators.required, Validators.minLength(5)])
      }, {
        validators: [this.passwordMatchValidator]
      })
    }


    /*  
       Validation 
     */

    // Custom Validation: Password Match 
    passwordMatchValidator(form: AbstractControl): ValidationErrors | null{
      const password = form.get('password')?.value
      const confirmPassword = form.get("confirmPassword")?.value
      if(password!=confirmPassword){
        return {passwordMismatch: true}
      }
      return null
    }

    isCtrlInvalid(ctrlName: string):boolean {
      return !!this.signUpForm.get(ctrlName)?.invalid
    }

    disableButton():boolean {
      return !!this.signUpForm.invalid
    }



    /*
     * Process form 
     */
    processSignUpForm() :void{

      console.info("Your username is ", this.signUpForm.controls['username'].value)
      console.info("Your email is ", this.signUpForm.controls['email'].value)
      console.info("Your password is ", this.signUpForm.controls['password'].value)


      const signUpDetails: SignUpDetails = {
        username: this.signUpForm.controls['username'].value,
        email: this.signUpForm.controls['email'].value,
        password: this.signUpForm.controls['password'].value
      }

      // Send the sign up details to the service  
      this.sub = this.userSvc.postSignUpDetails(signUpDetails).subscribe({
        next: (data)=> {
          console.info("This is ur message from server ", data)
          this.router.navigate(['/signUpSuccess'])
          this.signUpForm.reset()

        }, 
        error: (error)=> {
          console.info("This is your error ", error)
          this.errorMessage  = error
          this.signUpForm.reset()
        }, 
        complete: () => {
          
          console.info("Request completed")
          
        }
      })

    }

    checkErrorMessage(): boolean {
      return !!this.errorMessage
    }


    /**
     *   Route Gaurds 
     *    - When form is dirty (has been modified)
     * 
     */
    isDirty():boolean{
      return this.signUpForm.dirty
    }

    isInvalid():boolean{
      return this.signUpForm?.invalid
    }


    ngOnDestory(){
      this.sub.unsubscribe()
    }
}
