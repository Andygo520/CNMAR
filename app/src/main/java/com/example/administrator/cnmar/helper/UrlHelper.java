package com.example.administrator.cnmar.helper;

/**
 * Created by Administrator on 2016/9/29.
 */
public class UrlHelper {
//    public static String URL_BASE = "http://192.168.1.112:8092/";
    public static String URL_BASE = "http://benxiao.cnmar.com:8092/";

    public static final String URL_LOGIN =URL_BASE + "login_commit?username={username}&password={password}";
    public static final String URL_CHECK_STOCK = URL_BASE + "material_stock_check/detail/{ID}";
    public static final String URL_STOCK_CHECK_MANAGE =URL_BASE +  "material_stock_check_manage/detail/{ID}";
    public static final String URL_CHECK_COMMIT =URL_BASE +  "material_stock_check_manage/check_commit?stockId={stockId}&spaceStockIds={spaceStockIds}&spaceIds={spaceIds}&beforeStocks={beforeStocks}&afterStocks={afterStocks}";
    public static final String URL_IN_ORDER_DETAIL =URL_BASE +  "material_in_order/detail/{id}";
    public static final String URL_OUT_ORDER_DETAIL =URL_BASE +  "material_out_order/detail/{id}";
    public static final String URL_OUT_COMMIT =URL_BASE +  "material_out_order/out_stock_commit?outOrderId={outOrderId}&outOrderSpaceIds={outOrderSpaceIds}&preOutStocks={preOutStocks}&outStocks={outStocks}";
    public static final String URL_STOCK_DETAIL = URL_BASE + "material_stock/detail/{id}";
    public static final String URL_PRODUCT_CHECK_STOCK =URL_BASE +  "product_stock_check/detail/{ID}";
    public static final String URL_PRODUCT_CHECK_STOCK_MANAGE =URL_BASE +  "product_stock_check_manage/detail/{ID}";

    public static final String URL_PRODUCT_CHECK_STOCK_COMMIT =URL_BASE +  "product_stock_check_manage/check_commit?stockId={stockId}&spaceStockIds={spaceStockIds}&spaceIds={spaceIds}&beforeStocks={beforeStocks}&afterStocks={afterStocks}";

    public static final String URL_PRODUCT_STOCK_DETAIL = URL_BASE + "product_stock/detail/{id}";
    public static final String URL_TEST_COMMIT =URL_BASE +  "material_in_order/test_commit?inOrderId={inOrderId}&inOrderMaterialIds={inOrderMaterialIds}&res={res}&failNums={failNums}";
    public static final String URL_IN_ORDER_COMMIT =URL_BASE +  "material_in_order/in_stock_commit?inOrderId={inOrderId}&inOrderSpaceIds={inOrderSpaceIds}&preInStocks={preInStocks}&inStocks={inStocks}";
    public static final String URL_IN_ORDER =URL_BASE +  "material_in_order/list?query.code=&page.num=1";
    public static final String URL_SEARCH_IN_ORDER =URL_BASE +  "material_in_order/list?query.code={query.code}&page.num=1";
    public static final String URL_CHECK_MANAGE = URL_BASE + "material_stock_check_manage/list?query.code=&page.num=1";
    public static final String URL_SEARCH_CHECK_MANAGE =URL_BASE +  "material_stock_check_manage/list?query.code={query.code}&page.num=1";
    public static final String URL_CHECK_QUERY = URL_BASE + "material_stock_check/list?query.code=&page.num=1";
    public static final String URL_SEARCH_CHECK_QUERY =URL_BASE +  "material_stock_check/list?query.code={query.code}&page.num=1";

    public static final String URL_STOCK =URL_BASE +  "material_stock/list?query.code=&page.num=1";
    public static final String URL_SEARCH_STOCK = URL_BASE + "material_stock/list?query.code={query.code}&page.num=1";

    public static final String URL_OUT_ORDER = URL_BASE + "material_out_order/list?query.code=&page.num=1";
    public static final String URL_SEARCH_OUT_ORDER =URL_BASE +  "material_out_order/list?query.code={query.code}&page.num=1";

    public static final String URL_PRODUCT_CHECK_MANAGE =URL_BASE +  "product_stock_check_manage/list?query.code=&page.num=1";
    public static final String URL_PRODUCT_SEARCH_CHECK_MANAGE = URL_BASE + "product_stock_check_manage/list?query.code={query.code}&page.num=1";

    public static final String URL_PRODUCT_CHECK_QUERY =URL_BASE +  "product_stock_check/list?query.code=&page.num=1";
    public static final String URL_PRODUCT_SEARCH_CHECK_QUERY =URL_BASE +  "product_stock_check/list?query.code={query.code}&page.num=1";

    public static final String URL_PRODUCT_STOCK =URL_BASE +  "product_stock/list?query.code=&page.num=1";
    public static final String URL_PRODUCT_SEARCH_STOCK = URL_BASE + "product_stock/list?query.code={query.code}&page.num=1";

}
