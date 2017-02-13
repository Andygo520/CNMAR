package component.system.vo;

import org.apache.commons.lang.StringUtils;

public enum LogModuleVo {

	empty(null, ""),
	/** 用户 */
	system_user("system_user", "用户"),
	/** 角色 */
	system_role("system_role", "角色"),
	/** 菜单 */
	system_menu("system_menu", "菜单"),
	/** 权限 */
	system_auth("system_auth", "权限"),
	/** 单位 */
	unit("unit", "单位"),
	/** 原料抽样标准 */
	material_sample_standard("material_sample_standard", "原料抽样标准"),

	/** 企业 */
	company("company", "企业"),
	/** 产品 */
	company_product("company_product", "产品"),

	/** 供应商 */
	supply("supply", "供应商"),

	/** BOM属性 */
	category("category", "BOM属性"),
	/** 原料 */
	material("material", "原料"),
	/** 半成品 */
	half("half", "半成品"),
	/** 半成品BOM */
	bom_half("bom_half", "半成品BOM"),
	/** 成品 */
	product("product", "成品"),
	/** 成品BOM */
	bom_product("bom_product", "成品BOM"),

	/** 加工单 */
	produce_plan("produce_plan", "加工单"),
	/** 领料单退料 */
	produce_back("produce_back", "领料单退料"),
	/** 交付计划 */
	custom_deliver_plan("custom_deliver_plan", "交付计划"),

	/** 原料仓位 */
	material_space("material_space", "原料仓位"),
	/** 原料入库单 */
	material_in_order("material_in_order", "原料入库单"),
	/** 原料出库单 */
	material_out_order("material_out_order", "原料出库单"),
	/** 原料库存 */
	material_stock_check("material_stock_check", "原料库存"),

	/** 半成品仓位 */
	half_space("half_space", "半成品仓位"),
	/** 半成品入库单 */
	half_in_order("half_in_order", "半成品入库单"),
	/** 半成品出库单 */
	half_out_order("half_out_order", "半成品出库单"),
	/** 半成品库存 */
	half_stock_check("half_stock_check", "半成品库存"),

	/** 成品仓位 */
	product_space("product_space", "成品仓位"),
	/** 成品入库单 */
	product_in_order("product_in_order", "成品入库单"),
	/** 成品出库单 */
	product_out_order("product_out_order", "成品出库单"),
	/** 成品库存 */
	product_stock_check("product_stock_check", "成品库存");

	private String key;
	private String value;

	private LogModuleVo(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static final LogModuleVo getInstance(String key) {
		for (LogModuleVo vo : values()) {
			if (StringUtils.isEmpty(key)) {
				return empty;
			} else if (key.equals(vo.getKey())) {
				return vo;
			}
		}
		throw new RuntimeException();
	}

}
