import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../../service/UserService';
import { Router } from '@angular/router';
import { debounceTime, map, Subscription } from 'rxjs';
import { userDetails } from '../../model/models';

@Component({
  selector: 'app-login-component',
  standalone: false,
  templateUrl: './login-component.component.html',
  styleUrl: './login-component.component.css'
})
export class LoginComponentComponent {
      //Create a form fb 
      protected fb = inject(FormBuilder)
      private userSvc = inject(UserService)
      private router = inject(Router)
      
      protected mainForm!: FormGroup
      protected errorMessage!: string
    
      private statusSub!: Subscription
      private loginSub!:Subscription
      protected hidePassword = true; // For toggling password visibility
    
     
    
      ngOnInit(): void {
    
        this.mainForm = this.createForm()
    
        //Create and see whether the usertype is correct 
        this.statusSub = this.mainForm.statusChanges.pipe(
          debounceTime(2000),
          map(v => v == "VALID")
        )
        .subscribe({
          next: (changes)=> {
            console.info(">>> changes: ", changes)
          }
        })
    
      }
    
      // Create Form 
      createForm(): FormGroup {
    
        return this.fb.group({
          username: this.fb.control<string>("", [Validators.required, Validators.minLength(5)]),
          password: this.fb.control<string>("", [Validators.required, Validators.minLength(5)])
        })
      }
    
    
    
      // Process form 
      processForm(): void{
    
        // Create a model and pass this model to the login service -> pass this info -> springboot 
    
        const userDetails: userDetails ={
          username: this.mainForm.controls["username"].value, 
          password: this.mainForm.controls["password"].value
        }
    
        this.loginSub = this.userSvc.login(userDetails).subscribe({
          next: (data) => {
            console.info("This is your token ", data)
            console.info("Ur username is ", userDetails.username)
            
    
            // Store the JWT token in Local Storage
            localStorage.setItem("jwtToken",data.token)
    
            // Store the username also 
            localStorage.setItem("username",userDetails.username)
    
            // This token will be stored in localStorage and will be needed for subsequent REST API calling
            this.router.navigate(['/layout']) 
    
          }, 
          error: (error) => {
            console.info("This is your error message ", error)
            this.errorMessage = error
            this.mainForm.reset()
          }, 
          complete: () => {
            console.info("Your request has been completed")
          }
    
        })
        
    
    
      }
    
      // Validation 
      isCtrlInvalid(ctrlName: string):boolean {
        return !!this.mainForm.get(ctrlName)?.invalid
      }
    
      checkErrorMessage(): boolean {
        return !!this.errorMessage
      }
    
      ngOnDestroy(){
        this.loginSub?.unsubscribe()
        
      }
}
