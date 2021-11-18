package in.cjcj.sboa.wrap.rest.exception.custom;


import in.cjcj.sboa.wrap.rest.errors.codes.CommonResourceErrorCode;
import in.cjcj.sboa.wrap.rest.errors.codes.CustomErrorDetail;
import in.cjcj.sboa.wrap.rest.exception.base.InternalServerException;

/**
 * Thrown by BeanUtil Exception. All the exceptions by Apache Bean Util while copies properties
 * values from one bean to another bean are wrapped by this runtime exception
 */
public class BeanPropertiesCopyException extends InternalServerException {
    public BeanPropertiesCopyException() {
        super(CommonResourceErrorCode.BEAN_ID_COPY_ERROR);
    }

    public BeanPropertiesCopyException(Object source, Object destination) {
        super(new CustomErrorDetail(CommonResourceErrorCode.BEAN_ID_COPY_ERROR).developerMsg("" +
                "Something went wrong while copying bean properties from " + source.getClass().getName() + " to " + destination.getClass().getName()));
    }
}