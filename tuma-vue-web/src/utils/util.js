export const getTimer = (stringTime) => {
    let minute = 1000 * 60;
    let hour = minute * 60;
    let day = hour * 24;
    let week = day * 7;
    let month = day * 30;
    let time1 = new Date().getTime();
    let time2 = Date.parse(new Date(stringTime));
    let time = time1 - time2;
    let result = null;
    if (time / month >= 1) {
        result = parseInt(time / month) + "月前";
    } else if (time / week >= 1) {
        result = parseInt(time / week) + "周前";
    } else if (time / day >= 1) {
        result = parseInt(time / day) + "天前";
    } else if (time / hour >= 1) {
        result = parseInt(time / hour) + "小时前";
    } else if (time / minute >= 1) {
        result = parseInt(time / minute) + "分钟前";
    } else {
        result = "刚刚";
    }
    return result;
}

export const listToTree = (list) => {
    var map = {}, node, tree = [], i;
    for (i = 0; i < list.length; i++) {
        map[list[i].id] = list[i];
        list[i].children = [];
    }
    for (i = 0; i < list.length; i += 1) {
        node = list[i];
        if (node.pid !== '-1') {
            map[node.pid].children.push(node);
        } else {
            tree.push(node);
        }
    }
    return tree;
}
