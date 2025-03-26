import { Component, inject } from '@angular/core';
import { PriceCompareService } from '../../../service/PriceComparisonService';
import { Observable, Subscription } from 'rxjs';
import { PriceComparisonResult } from '../../model/models';

@Component({
  selector: 'app-compare-price-result',
  standalone: false,
  templateUrl: './compare-price-result.component.html',
  styleUrl: './compare-price-result.component.css'
})
export class ComparePriceResultComponent {

  private priceCompareSvc = inject(PriceCompareService)
  
  private sub!:Subscription
  protected results$! : Observable<PriceCompareService>
  protected isLoading = false

  protected priceCompareResult! : PriceComparisonResult | null

   ngOnInit(): void {
    this.isLoading = true
    this.priceCompareResult = null

    this.sub = this.priceCompareSvc.priceComparisonResult.subscribe({
      next: (result)=>{

        if(result) {
          this.priceCompareResult = result
          this.isLoading= false
        }
       
      }
    })
   }


   getIngredients(results: PriceComparisonResult): string[] {
    return Object.keys(results);
  }
}
