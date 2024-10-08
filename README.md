# MonkAPI - Buy X Get Y (BxGy) Coupon System

MonkAPI is a Spring Boot-based application designed to manage and apply various types of coupons to a shopping cart. The application includes services for validating, applying, and managing coupons, as well as mapping POJOs to DTOs.

## Features

- [Project Structure](#project-structure)
- [Endpoints](#endpoints)
- [Implemented Cases](#implemented-cases)
- [Limitations](#limitations)
- [Assumptions](#assumptions)
---

## Project Structure
```plaintext
src/
└── main/
   ├── java/
   │   └── com/
   │       └── example/
   │           └── MonkAPI/
   │               ├── controller/
   │               │   └── CouponController.java
   │               ├── dto/
   │               │   └── (DTO classes)
   │               ├── pojo/
   │               │   └── (POJO classes)
   │               ├── repository/
   │               │   └── CouponRepository.java
   │               ├── service/
   │               │   ├── ApplyCouponBasedOnIdService.java
   │               │   ├── CouponService.java
   │               │   ├── PojoToDtoService.java
   │               │   └── ValidatingCouponsService.java
   │               └── MonkApiApplication.java
   └── resources/
       ├── application.properties
       └── (other resource files)
```
## Endpoints
### 1. **CouponController**
- **POST /coupons**: Creates a new coupon.
  - **Request Body**: JSON representation of the coupon.
  - **Example Input:**
  ```json
  {
    "type": "cart-wise",
    "details": {
        "threshold": 80,
        "discount": 20
    }
  }
  ```
  ```json
  {
    "type": "product-wise",
    "details": {
        "product_id": 2,
        "discount": 21
    }
  }
  ```
  ```json
  {
    "type": "bxgy",
    "details": {
        "buy_products": [
            {"product_id": 1,"quantity": 2},
            {"product_id": 2,"quantity": 2}
        ],
        "get_products": [
            {"product_id": 3,"quantity": 1}
        ],
    "repition_limit": 2
    }
    }
  ```

  - **Response**: The created coupon with an assigned ID.
### 2. **ApplyCouponsController**
- **POST /applicable-coupons**: Applies coupons to the given cart.
  **Example Input**:
    ```json
    {
        "cart": {
            "items": [
                {
                    "product_id": 2,
                    "quantity": 4,
                    "price": 50
                },
                {
                    "product_id": 1,
                    "quantity": 2,
                    "price": 50
                },
                {
                    "product_id": 3,
                    "quantity": 1,
                    "price": 30
                }
            ]
        }
    }
    ```
- **Response**: The applied coupon with an assigned ID.

  ```json
    {
    "validCouponsList": [
        {
            "coupon_id": 1,
            "type": "cart-wise",
            "discount": 20
        },
        {
            "coupon_id": 2,
            "type": "product-wise",
            "discount": 21
        },
        {
            "coupon_id": 3,
            "type": "bxgy",
            "discount": 30
        },
        {
            "coupon_id": 3,
            "type": "bxgy",
            "discount": 30
        }
      ]
    }
  ```
### 2. **ApplyCouponsController**
- **POST /apply-coupon/{id}**: Applies a coupon to the given cart based on the coupon ID. 
- **Example Input**: /apply-coupon/3
  ```json
     {
        "cart": {
            "items": [
                {
                    "product_id": 1,
                    "quantity": 6,
                    "price": 50
                },
                {
                    "product_id": 2,
                    "quantity": 3,
                    "price": 30
                },
                {
                    "product_id": 3,
                    "quantity": 2,
                    "price": 25
                }
             ]
         }
     }
    ```
- **Response**: The result of coupon application.
  - **Example Response**:
  ```json
    {
        "items": [
            {
                "productId": 1,
                "quantity": 6,
                "price": 50,
                "totalDiscount": 0
            },
            {
                "productId": 3,
                "quantity": 4,
                "price": 25,
                "totalDiscount": 50
            },
            {
                "productId": 2,
                "quantity": 3,
                "price": 30,
                "totalDiscount": 0
            }
        ],
        "totalPrice": 490,
        "totalDiscount": 50,
        "finalPrice": 440
    }
    ```


## Implemented Cases

### 1. **Buy X, Get Y (BxGy) Coupons**
- The system supports **BxGy** coupons where users get one or more products for free when they buy specific products in the required quantity.

- **Logic**:
    - If the product in the request is listed in the `BuyProduct` database and the requested quantity meets or exceeds the required quantity, the system checks if the associated `GetProduct` is present in the request.
    - If both conditions are met, the system applies a discount equivalent to the price of the free products (`GetProduct`).

### 2. **ApplyCoupons Based On Coupon Id**
- POST /apply-coupon/{id}: Applies a coupon to the given cart based on the coupon ID.
### 3. **Limit Exceed Check for BxGy Coupons**
- POST /apply-coupon/{id}: Applies a coupon to the given cart based on the coupon ID.
- **Logic**:
    - Each BxGy coupon has a repetition_limit field that specifies the maximum number of times the coupon can be applied.
    - During coupon application, the system checks the number of times the coupon has already been applied.
    - If the number of applications exceeds the repetition_limit, the coupon is not applied

## Limitations

1. **Handling Existing `GetProduct` in Cart for BxGy Coupons**:
    - - The system does not currently handle the scenario where the `GetProduct` is already present in the cart. The discount is not subtracted or applied to the existing quantity; instead, more items are added to the cart.

2. **Discount as a Number**:
    - The discount value is treated as a fixed amount, not a percentage. For example, if the discount is set to 10, it means the amount will be reduced by 10 units, not 10% of the order value.

3. **Limited Input Validation**:
    - Due to focusing on implementing the main logic, there are not many input validations. The system expects correct input only.

4. **No Support for Multiple Cart Wise Coupon Types**:
    - The current implementation only supports Single Cart Wise coupons.
---

## Assumptions

1.**Valid Input Only**:
- The system assumes that all inputs are valid due to time constraints. There is limited input validation implemented.

---

