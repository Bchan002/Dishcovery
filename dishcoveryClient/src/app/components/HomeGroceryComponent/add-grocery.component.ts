import { Component, inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { GroceryService } from '../../../service/GroceryService';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subscription } from 'rxjs';
import { AddGroceryItem } from '../../model/models';

@Component({
  selector: 'app-add-grocery',
  standalone: false,
  templateUrl: './add-grocery.component.html',
  styleUrl: './add-grocery.component.css'
})
export class AddGroceryComponent implements OnInit {

    //displayedColumns: string[] = ['name', 'quantity', 'category', 'expirationDate', 'remove'];
  
  // Inject and Create the form 
  private fb = inject(FormBuilder)
  private grocerySvc = inject(GroceryService)
  private snackBar = inject(MatSnackBar)
  protected groceryForm!: FormGroup
  //protected groceryArray!: FormArray
  protected grocerySub!: Subscription
  protected groceryArray: AddGroceryItem[] = []

  ngOnInit(): void {

    this.groceryForm = this.createForm()

  }


  createForm(): FormGroup {
    //Create a placeHolder Date 
    const todayDate = new Date()
    const formattedDate = todayDate.toISOString().substring(0, 10)

    //Initialize the array 
    //this.groceryArray = this.fb.array([])

    return this.fb.group({
      name: this.fb.control<string>("", Validators.required),
      category: this.fb.control<string>("", Validators.required),
      quantity: this.fb.control<number>(0, [Validators.required, Validators.min(1)]),
      expirationDate: this.fb.control<string>(formattedDate, Validators.required),
      //groceryArray: this.groceryArray

    },
      {
        validators: [this.dateChecker]
      }
    )
  }

  /**
   * 
   *  Validation
   */
  //Check the date whether is it in the past 
  dateChecker(form: AbstractControl): ValidationErrors | null {
    const date = form.get("expirationDate")?.value
    const expirationDate: Date = new Date(date)
    const todayDate = new Date()

    //Set both dates to midnight
    expirationDate.setHours(0, 0, 0, 0)
    todayDate.setHours(0, 0, 0, 0)

    if (expirationDate < todayDate) {
      return { pastDueDate: true }
    }

    return null
  }
  
  isCtrlInvalid(ctrlName: string):boolean {
    return !!this.groceryForm.get(ctrlName)?.invalid
  }

  disableButton():boolean {
    return !!this.groceryForm.invalid
  }


  createGrocery(groceryItem: AddGroceryItem): FormGroup {
    return this.fb.group({
      itemName: this.fb.control<string>(groceryItem.itemName, Validators.required),
      category: this.fb.control<string>(groceryItem.category, Validators.required),
      quantity: this.fb.control<number>(groceryItem.quantity, Validators.required),
      expirationDate: this.fb.control<string>(groceryItem.expirationDate, Validators.required),
    })
  }


  addGroceryItem(): void {

    // console.info("Your description is ", this.groceryForm.controls["name"]?.value)
    // console.info("Your priority is ", this.mainForm.controls["priority"]?.value)
    // console.info("Your due date is ", this.mainForm.controls["dueDate"]?.value)

    const groceryItem: AddGroceryItem = {
      itemName: this.groceryForm.controls["name"]?.value,
      category: this.groceryForm.controls["category"]?.value,
      quantity: this.groceryForm.controls["quantity"]?.value,
      expirationDate: this.formatDate(this.groceryForm.controls["expirationDate"]?.value),

    }

    //this.groceryArray.push(this.createGrocery(groceryItem))
    this.groceryArray = [...this.groceryArray, groceryItem]
    //Reset only the main form controls
    // this.groceryForm.patchValue({
    //   name: '',
    //   category: '',
    //   quantity: 0,
    //   expirationDate: new Date().toISOString().substring(0, 10)
    // })


    this.groceryForm.markAsPristine();
    this.groceryForm.markAsUntouched();
    this.groceryForm = this.createForm()

    this.groceryForm.updateValueAndValidity

  
  }

  // Converts tp YYYY-MM-DD
  formatDate(date: any): string {
    if (!date) return '';
    const d = new Date(date);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }


  removeItem($event: number) {
    console.info("Your item index to remvove is ", $event)

    this.groceryArray.splice($event,1)

    // Change detection 
    this.groceryArray = [...this.groceryArray]

    // Update the Form Array reference 
    //this.groceryForm.setControl("groceryArray", this.groceryArray)
  }


  saveGroceryItem():void{
    if (this.groceryArray.length === 0) {
      console.warn("No items to save!");
      return;
    }



    // Extract the items from FormArray
    //const groceryItems: AddGroceryItem[] = this.groceryArray.value;

    // Send data to service
    // this.groceryService.saveGroceryItems(groceryItems).subscribe({
    //   next: (response) => {
    //     console.info("Grocery items saved successfully!", response);
    //     this.groceryArray.clear(); // Clear form array after saving
    //   },
    //   error: (error) => {
    //     console.error("Error saving grocery items", error);
    //   }
    // });
    this.grocerySub = this.grocerySvc.saveGroceryItem(this.groceryArray).subscribe({
      next: (response)=>{
        console.info("grocery items saved successfully! ", response)
        
        this.snackBar.open('Grocery saved successfully!', 'Close', {
          duration: 3000,  
          verticalPosition: 'top',  
          horizontalPosition: 'center'  
        });
        this.groceryArray = []
      }, 
      error: (error) => {
        console.info("Error saving grocery items ", error)
      }
    })
  
  }

  ngOnDestroy():void{
    if(this.grocerySub){
      this.grocerySub.unsubscribe()
    }
  }
}
