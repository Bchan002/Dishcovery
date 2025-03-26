import { inject, Injectable } from "@angular/core";

import { BehaviorSubject, map, Observable, tap } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { PriceCompare, PriceComparisonResult } from "../app/model/models";
import { environmentProd } from "../app/environments/environment.prod";

@Injectable()
export class PriceCompareService {
    
    private http = inject(HttpClient)

    private COMPARE_PRICE =  `${environmentProd.apiUrl}/comparePrice`
    // private COMPARE_PRICE = "http://localhost:8080/comparePrice"

    priceComparisonResult = new BehaviorSubject<PriceComparisonResult | null>(null)
    
    checkPrice(priceCompare: PriceCompare): Observable<PriceComparisonResult> {

        this.priceComparisonResult.next(null)

        return this.http.post<PriceComparisonResult>(this.COMPARE_PRICE, priceCompare).pipe(
          tap(rawResult => {
            console.info("Here is your result ", rawResult);
      
            const transformed: PriceComparisonResult = {};
      
            Object.entries(rawResult).forEach(([ingredient, products]) => {
              transformed[ingredient] = products.map((p: any) => ({
                productName: p["Product Name"],
                productPrice: p["Product Price"],
                imageUrl: p["image url"],
                inStock: p["in-stock"],
                offer: p["offer"],
                productUrl: p["product url"]
              }));
            });
      
            this.priceComparisonResult.next(transformed);
          })
        );
      }
      
}