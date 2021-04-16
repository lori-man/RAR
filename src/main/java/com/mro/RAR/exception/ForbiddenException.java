package com.mro.RAR.exception;

/**
 * @ClassName: com.mro.RAR.exception
 * @Description:
 * @author: 福建采控网络科技有限公司
 * @date: 2021-01-06 0006 09:25:49
 * 创建人: xfs
 * @Copyright: 2021 www.ckmro.com Inc. All rights reserved.
 * 注意：本内容仅限于福建采控网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class ForbiddenException extends GatewayLimitException {
    public ForbiddenException() {

    }

    public ForbiddenException(String message) {
        super(message);
    }
}
