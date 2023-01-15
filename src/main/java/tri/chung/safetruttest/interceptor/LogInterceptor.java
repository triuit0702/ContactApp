package tri.chung.safetruttest.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LogInterceptor implements HandlerInterceptor {

	private static Logger log = LoggerFactory.getLogger(LogInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		return true;
	}

	/**
	 * Executed before after handler is executed
	 **/
	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
			final ModelAndView modelAndView) throws Exception {
		log.info("[postHandle][" + request + "]");
	}

	/**
	 * Executed after complete request is finished
	 **/
	@Override
	public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
			final Object handler, final Exception ex) throws Exception {

		log.info("afterCompletion");
	}
}
