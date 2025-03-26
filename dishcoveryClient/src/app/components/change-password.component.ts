import { Component, inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { UserService } from '../../service/UserService';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-change-password',
  standalone: false,
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.css'
})
export class ChangePasswordComponent implements OnInit {

    private fb = inject(FormBuilder)
    protected form! : FormGroup
    hidePassword = true;
    hideConfirm = true;
    private userSvc = inject(UserService)
    private snackBar = inject(MatSnackBar)
    private dialogRef = inject(MatDialogRef<ChangePasswordComponent>);


    ngOnInit():void{

       this.form = this.createForm()
    }



    createForm():FormGroup{
      return this.fb.group({
        password: this.fb.control<string>('', [Validators.required, Validators.minLength(5)]),
        confirmPassword: this.fb.control<string>('', [Validators.required, Validators.minLength(5)])
      }, {
        validators: [this.passwordMatchValidator]
      })
    }

    passwordMatchValidator(form: AbstractControl): ValidationErrors | null{
      const password = form.get('password')?.value
      const confirmPassword = form.get("confirmPassword")?.value
      if(password!=confirmPassword){
        return {passwordMismatch: true}
      }
      return null
    }

    isCtrlInvalid(ctrlName: string):boolean {
      return !!this.form.get(ctrlName)?.invalid
    }

    disableButton():boolean {
      return !!this.form.invalid
    }

    submitChange(): void {
      if (this.form.valid) {
        const newPassword = this.form.get('password')?.value;
        
        this.userSvc.changePassword(newPassword).subscribe({
          next:(response=>{
            console.info("This is your resposne ", response)
            this.snackBar.open('Password Changed successfully!', 'Close', {
              duration: 3000,  
              verticalPosition: 'top',  
              horizontalPosition: 'center'  
            });

            this.dialogRef.close()
            
          }), 
          error: (error=>{
            console.info("this is your error ", error)
            alert("Change Password Error")
          })
        })
        
      }
    }

}
