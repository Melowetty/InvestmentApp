package com.melowetty.investment.models

data class ProfileModel(val symbol: String,
                        val companyName: String,
                        val price: Double,
                        val changes: Double,
                        val image: String,
                        val currency: String)
