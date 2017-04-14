package component.common.vo;

public interface MsgCode {

	public static final String success = "成功";

	public static final String token_error = "token错误";

	public static final String username_exist = "账号已存在";
	public static final String delete_subs = "请先删除下级数据";

	public static final String system_error = "服务器内部错误";
	public static final String password_error = "账号和密码不匹配";

	public static final String param_error = "参数错误";
	public static final String status_error = "状态错误";

	public static final String scan_error = "二维码已扫描";
	public static final String stock_error = "库存不足";
	public static final String space_error = "仓位不足";

	public static final String task_error = "该机台没有您的加工任务";
	public static final String user_error = "非空料框只能被同一操作工使用";
	public static final String different_error = "非空料框只能放入同一机台同一工序的在制品";
	public static final String not_prev_error = "只能扫描上一道工序的料框";

}
