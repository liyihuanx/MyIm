package liyihuan.app.android.lib_im.utils

import com.google.gson.Gson
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.ArrayList

/**
 * @ClassName: TypeUtils
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/12/20 23:10
 */
object TypeUtils {

    /**
     * find the type by interfaces
     * @param cls
     * @param <R>
     * @return
    </R> */
    fun <R> findNeedType(cls: Class<R>): Type? {
        val typeList = getMethodTypes(cls)
        return if (typeList == null || typeList.isEmpty()) {
            null
        } else typeList[0]
    }

    /**
     * MethodHandler
     */
    private fun <T> getMethodTypes(cls: Class<T>): List<Type>? {
        val typeOri = cls.genericSuperclass
        var needtypes: MutableList<Type>? = null
        // if Type is T
        if (typeOri is ParameterizedType) {
            needtypes = ArrayList()
            val parentypes = typeOri.actualTypeArguments
            for (childtype in parentypes) {
                needtypes.add(childtype)
                if (childtype is ParameterizedType) {
                    val childtypes = childtype.actualTypeArguments
                    for (type in childtypes) {
                        needtypes.add(type)
                    }
                }
            }
        }
        return needtypes
    }

    @JvmStatic
    val gson = Gson()



}