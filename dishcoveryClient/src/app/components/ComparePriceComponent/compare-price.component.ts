import { Component, inject } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-compare-price',
  standalone: false,
  templateUrl: './compare-price.component.html',
  styleUrl: './compare-price.component.css'
})
export class ComparePriceComponent {

  private fb = inject(FormBuilder)
  protected priceData = []

  dialogRef = inject(MatDialogRef<ComparePriceComponent>)

  protected form!:FormGroup

  ngOnInit(): void {
      this.form = this.createForm()
  }

  createForm():FormGroup {
    return this.fb.group({
      minPrice: this.fb.control<string>("", [Validators.required,Validators.min(0)]),
      maxPrice: this.fb.control<string>("",[Validators.required,Validators.min(0)])

    }, 
    {
      validators: [this.priceRangeValidator]
    }
  )
  }

  //Customize validation 
  priceRangeValidator(form: AbstractControl): ValidationErrors | null{
    
    const minPrice = form.get("minPrice")?.value
    const maxPrice = form.get("maxPrice")?.value

    if(minPrice && maxPrice && Number(minPrice)>=Number(maxPrice)){
      return {invalidPriceRange:true}
    }

    return null
  }

  isCtrlInvalid(ctrlName: string):boolean {
    return !!this.form.get(ctrlName)?.invalid
  }

  disabled():boolean {
    return !!this.form?.invalid
  }

  onCancel(): void {
    this.dialogRef.close()
  }

  onCompare(): void {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value);
    }


  }
}
