//对表单时间数据进行格式化
import MessageBox from "element-ui/packages/message-box/src/main";
import Message from "element-ui/packages/message/src/main";
import axios from "axios";

let messageBox = MessageBox;
let message = Message;

export const tableDateFormat = (row, column, cellValue, index) => {
    if (!cellValue) return "";
    let temp = cellValue.substr(0, cellValue.indexOf('.')).replace('T', ' ')
    return temp;
}
//判断字符串是否只存在数字
//数字包括整形和浮点型
//javascript怎么判断字符串是否为数字？
//https://www.html.cn/qa/javascript/11725.html
export const numberReg = (value) => {
    return /^[0-9]+\.?[0-9]*$/.test(value)
};
//两个对象只要有一个属性值对不上就返回false
//只有属性值都相等才返回true
export const same = (source, target) => {
    let same = true
    let strings = Object.keys(source);
    let strings1 = Object.keys(target);
    if (strings.length != strings1.length) return false
    for (let i = 0; i < strings.length; i++) {
        let it = strings[i];
        if (!(source[it] == target[it])) return !same
    }
    return same;
}
//不支持嵌套对象
//只要对象为null对象或者有某个属性值为空
//返回true
export const isNull = (object) => {
    if (object instanceof Array && !object.length) return true
    let temp = Object.keys(object);
    if (!temp.length) return true
    return temp.some(it => {
        let tempValue = object[it];
        if (typeof tempValue == "boolean") tempValue = tempValue + ''
        if (!tempValue) return true
    })
}
//不支持嵌套对象
//如果对象所有属性值都为null
//返回true
export const isAllNull = (object) => {
    if (object instanceof Array && !object.length) return true
    let temp = Object.keys(object);
    if (!temp.length) return true
    return temp.some(it => {
        let tempValue = object[it];
        if (typeof tempValue == "boolean") tempValue = tempValue + ''
        if (tempValue) return false
    })
}
//将每个属性赋值为空字符串
export const toNull = (object) => {
    Object.keys(object).forEach(it => {
        object[it] = ''
    })
}
export const clone = (object) => {
    return JSON.parse(JSON.stringify(object))
}
/**
 * 以source key为基准,
 * 将target相同key的值赋值给source
 * @param source
 * @param target
 */
export const copy = (source, target) => {
    let keys = Object.keys(source)
    keys.forEach(it => {
        source[it] = target[it]
    })
}

export const deleteConfirm = (operation) => {
    messageBox.confirm('此操作将永久删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(operation).catch(() => {
        message({
            type: 'info',
            message: '已取消删除'
        });
    });
}
/**
 * 根据对象生成请求参数uri
 * @param obj
 * @returns {string}
 */
export const getUri = (obj) => {
    let uri = ''
    Object.keys(obj).forEach(it => {
        uri += it + '=' + obj[it] + '&'
    })
    return uri
}
/**
 * 对同域的图片文件做内存缓存
 * @param url
 * @returns {Promise<void>}
 */
export const urlToFile = async (url) => {
    await axios.get(url).then(data => {
        let index = url.lastIndexOf('.'), fileType = url.substr(index),
            fileName = url.substring(url.lastIndexOf('/'), index);
        let blob = new Blob([data]);
        return new File([blob], fileName, {type: fileType});
    })
}
/**
 *
 * @param obj
 * @returns {string}
 */
export const  setImgCache = (url,obj,propName) => {
    if (url.indexOf('blob') == -1&&url.indexOf('https')==-1)
         fetch(url).then(response => response.blob()).then(blob => {
            obj[propName] = URL.createObjectURL(blob);
        });
}

const install = (vue, ops = {}) => {
    vue.prototype.utils = {
        tableDateFormat: tableDateFormat,
        same: same,
        isNull: isNull,
        isAllNull: isAllNull,
        deleteConfirm: deleteConfirm,
        toNull: toNull,
        clone: clone,
        getUri: getUri,
        numberReg: numberReg,
        copy: copy,
        urlToFile: urlToFile,
        setImgCache:setImgCache,
    }
}


export const formatRoutes = function formatRoutes(obj, routes) {
    let fmtRoutes = []
    routes.forEach(route => {
        //path=route.path
        let {
            path,
            name,
            component,
            meta,
            iconCls,
            children,
        } = route
        if (children && children instanceof Array) {
            children = formatRoutes(obj, children);
        }
        let fmRouter = {
            path: path,
            name: name,
            iconCls: iconCls,
            meta: meta,
            children: children,
            //Es6动态导入
            component: () => import('../views/menu/' + path.split('/')[1] + '/' + component + '.vue')
            //Es5动态导入
            /*            component(resolve) {
                            if (component.startsWith('Emp'))
                                require(['../views/menu/emp/' + component + '.vue'], resolve)
                            else if (component.startsWith('Per'))
                                require(['../views/menu/per/' + component + '.vue'], resolve)
                            else if (component.startsWith('Sal'))
                                require(['../views/menu/sal/' + component + '.vue'], resolve)
                            else if (component.startsWith('Sta'))
                                require(['../views/menu/sta/' + component + '.vue'], resolve)
                            else if (component.startsWith('Sys'))
                                require(['../views/menu/sys/' + component + '.vue'], resolve)
                        },*/
        }
        if (!children) {
            obj.children.push(fmRouter);
        }
        fmtRoutes.push(fmRouter)
    })
    return routes;
}


export default {
    install,
}