package com.melowetty.investment

class CompanyProfile(val ticker: String) {
    private val TAG = this::class.java.simpleName
    var logo: String? = null
    var company: String? = null
    var currency: Currency? = null

    init {
        //ApiClient.apiKey["token"] = "c14aug748v6t8t43aqqg"
        // TODO БУДЕТ ЗАМЕНЕНО НА RETROFIT, ПОТОМУ ЧТО КЛИЕНТ ФИНХАБА НЕ РАБОТАЕТ НА АНДРОИДЕ
        //logo = profile!!.logo
        //company = profile!!.name
        //currency = Currency.getCardTypeByName(profile!!.currency!!)
    }
    fun getStockItem(): StockItem = StockItem(ticker, company, logo, 256.0, 2.01, true)
}