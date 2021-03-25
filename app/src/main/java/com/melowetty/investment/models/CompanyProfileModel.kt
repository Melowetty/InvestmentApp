package com.melowetty.investment.models

data class CompanyProfileModel(val symbol: String,
                               val companyName: String,
                               val price: Double,
                               val changes: Double,
                               val image: String,
                               val currency: String)
